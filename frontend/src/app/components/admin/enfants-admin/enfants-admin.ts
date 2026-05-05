import { ChangeDetectorRef, Component, computed, OnInit, signal } from '@angular/core';
import { ChildService } from '../../../apiServices/child-service';
import { Child } from '../../../models/child.model';
import { ActivityRegistration, RegistrationService } from '../../../apiServices/registration-service';

@Component({
  selector: 'app-enfants-admin',
  standalone: false,
  templateUrl: './enfants-admin.html',
  styleUrl: './enfants-admin.css',
})
export class EnfantsAdmin implements OnInit {

  childrens = signal<Child[]>([]);
  searchValue = signal('');

  // Inscriptions EN_ATTENTE regroupées par childId
  inscriptionsParEnfant = signal<Map<number, ActivityRegistration[]>>(new Map());

  // État du modal "motif d'annulation"
  motifModalOuvert = signal(false);
  motifTexte = signal('');
  inscriptionAAnnuler = signal<ActivityRegistration | null>(null);

  // Filtre local : recherche par prénom/nom
  searchChildren = computed(() => {
    const search = this.searchValue().toLowerCase().trim();
    if (!search) return this.childrens();
    return this.childrens().filter(c =>
      c.firstName.toLowerCase().includes(search) ||
      c.lastName.toLowerCase().includes(search)
    );
  });

  constructor(
    private childService: ChildService,
    private registrationService: RegistrationService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.chargerEnfants();
    this.chargerInscriptionsAttente();
  }

  chargerEnfants() {
    this.childService.getAllChildren().subscribe({
      next: data => this.childrens.set(data),
      error: () => this.childrens.set([])
    });
  }

  // Récupère les inscriptions EN_ATTENTE et les regroupe par childId
  chargerInscriptionsAttente() {
    this.registrationService.getPending().subscribe({
      next: (data) => {
        const map = new Map<number, ActivityRegistration[]>();
        for (const r of data) {
          if (!map.has(r.childId)) map.set(r.childId, []);
          map.get(r.childId)!.push(r);
        }
        this.inscriptionsParEnfant.set(map);
        this.cdr.detectChanges();
      },
      error: () => {},
    });
  }

  // Helper template : inscriptions en attente d'un enfant donné
  inscriptionsDe(childId: number): ActivityRegistration[] {
    return this.inscriptionsParEnfant().get(childId) || [];
  }

  // Confirme une inscription EN_ATTENTE → courriel au parent
  // Retourne 409 si la séance est complète
  confirmer(reg: ActivityRegistration) {
    if (!confirm(`Confirmer l'inscription de ${reg.childFullName} à "${reg.activityName}" ?\nLe parent recevra un courriel.`)) return;
    this.registrationService.adminConfirm(reg.id).subscribe({
      next: () => {
        this.retirerInscription(reg);
        alert('Inscription confirmée. Courriel envoyé au parent.');
      },
      error: (err) => {
        if (err?.status === 409) {
          alert(err.error?.error || 'Cette séance est complète. Impossible de confirmer.');
        } else {
          alert('Erreur lors de la confirmation');
        }
      },
    });
  }

  // Ouvre le modal pour saisir le motif d'annulation
  annuler(reg: ActivityRegistration) {
    this.inscriptionAAnnuler.set(reg);
    this.motifTexte.set('');
    this.motifModalOuvert.set(true);
  }

  fermerMotifModal() {
    this.motifModalOuvert.set(false);
    this.inscriptionAAnnuler.set(null);
    this.motifTexte.set('');
  }

  // Valide le motif et envoie l'annulation
  confirmerAnnulation() {
    const reg = this.inscriptionAAnnuler();
    if (!reg) return;

    this.registrationService.adminCancel(reg.id, this.motifTexte().trim()).subscribe({
      next: () => {
        this.retirerInscription(reg);
        this.fermerMotifModal();
      },
      error: () => alert('Erreur lors de l\'annulation'),
    });
  }

  // Retire une inscription du Map après confirmation/annulation
  private retirerInscription(reg: ActivityRegistration) {
    const map = new Map(this.inscriptionsParEnfant());
    const liste = (map.get(reg.childId) || []).filter(r => r.id !== reg.id);
    if (liste.length > 0) map.set(reg.childId, liste);
    else map.delete(reg.childId);
    this.inscriptionsParEnfant.set(map);
    this.cdr.detectChanges();
  }

  deleteChild(id: number) {
    this.childService.deleteChild(id).subscribe({
      next: () => this.childrens.set(this.childrens().filter(c => c.id !== id)),
      error: () => {},
    });
  }
}
