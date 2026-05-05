import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MessageService } from '../../../apiServices/message-service';

interface Destinataire {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
}

@Component({
  selector: 'app-messages-animateur',
  standalone: false,
  templateUrl: './messages-animateur.html',
  styleUrl: './messages-animateur.css',
})
export class MessagesAnimateur implements OnInit {

  parents: Destinataire[] = [];
  admins: Destinataire[] = [];

  cible: 'tous-parents' | 'tous-admins' | 'parent' | 'admin' | 'plusieurs-parents' = 'tous-parents';
  recipientId: number | null = null;
  parentIdsSelectionnes: number[] = []; // IDs cochés pour "plusieurs parents"
  sujet = '';
  contenu = '';
  type: 'ANNONCE' | 'RAPPEL' | 'ANNULATION' = 'ANNONCE';

  enCours = false;
  messageRetour = '';
  erreur = '';

  constructor(
    private messageService: MessageService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // Charger la liste des parents pour le select "Un parent"
    this.messageService.getParentsForAnim().subscribe({
      next: (data) => {
        console.log('Parents chargés :', data);
        this.parents = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur chargement parents :', err);
        this.erreur = 'Impossible de charger la liste des parents (status ' + err.status + ')';
        this.cdr.detectChanges();
      },
    });
    // Charger la liste des admins
    this.messageService.getAdminsForAnim().subscribe({
      next: (data) => { this.admins = data; this.cdr.detectChanges(); },
      error: (err) => console.error('Erreur chargement admins :', err),
    });
  }

  changerCible(): void {
    this.recipientId = null;
    this.parentIdsSelectionnes = [];
  }

  // Cases à cocher pour "Plusieurs parents"
  toggleParent(id: number, event: Event): void {
    const checked = (event.target as HTMLInputElement).checked;
    if (checked) {
      this.parentIdsSelectionnes.push(id);
    } else {
      this.parentIdsSelectionnes = this.parentIdsSelectionnes.filter(pid => pid !== id);
    }
  }

  envoyer(): void {
    this.messageRetour = '';
    this.erreur = '';

    if (!this.sujet.trim() || !this.contenu.trim()) {
      this.erreur = 'Le sujet et le message sont obligatoires';
      return;
    }
    if ((this.cible === 'parent' || this.cible === 'admin') && !this.recipientId) {
      this.erreur = 'Choisis un destinataire dans la liste';
      return;
    }
    if (this.cible === 'plusieurs-parents' && this.parentIdsSelectionnes.length === 0) {
      this.erreur = 'Coche au moins un parent';
      return;
    }

    this.enCours = true;
    const ok = (res: { message: string }) => this.terminer(res.message);
    const fail = () => this.echec();

    switch (this.cible) {
      case 'tous-parents':
        this.messageService.envoyerATousParentsAnim(this.sujet, this.contenu, this.type)
          .subscribe({ next: ok, error: fail });
        break;
      case 'tous-admins':
        this.messageService.envoyerATousAdminsAnim(this.sujet, this.contenu, this.type)
          .subscribe({ next: ok, error: fail });
        break;
      case 'parent':
        this.messageService.envoyerAUnParentAnim(this.recipientId!, this.sujet, this.contenu, this.type)
          .subscribe({ next: ok, error: fail });
        break;
      case 'admin':
        this.messageService.envoyerAUnAdminAnim(this.recipientId!, this.sujet, this.contenu, this.type)
          .subscribe({ next: ok, error: fail });
        break;
      case 'plusieurs-parents':
        // Envoi à plusieurs parents cochés
        this.messageService.envoyerAPlusieursParentsAnim(this.parentIdsSelectionnes, this.sujet, this.contenu, this.type)
          .subscribe({ next: ok, error: fail });
        break;
    }
  }

  private terminer(msg: string): void {
    this.enCours = false;
    this.messageRetour = msg;
    this.sujet = '';
    this.contenu = '';
    this.recipientId = null;
    this.parentIdsSelectionnes = [];
    this.cdr.detectChanges();
    // Toast auto-fade après 3 secondes
    setTimeout(() => {
      this.messageRetour = '';
      this.cdr.detectChanges();
    }, 3000);
  }

  private echec(): void {
    this.enCours = false;
    this.erreur = 'Erreur lors de l\'envoi';
    this.cdr.detectChanges();
  }
}
