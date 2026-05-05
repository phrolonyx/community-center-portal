import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MessageService } from '../../../apiServices/message-service';
import { UserService } from '../../../apiServices/user-service';
import { User } from '../../../models/user.model';

@Component({
  selector: 'app-messages-admin',
  standalone: false,
  templateUrl: './messages-admin.html',
  styleUrl: './messages-admin.css',
})
export class MessagesAdmin implements OnInit {

  // Listes pour les selects
  parents: User[] = [];
  animateurs: User[] = [];

  // Champs du formulaire
  cible: 'tous-parents' | 'tous-animateurs' | 'parent' | 'animateur' | 'plusieurs-parents' = 'tous-parents';
  recipientId: number | null = null;
  parentIdsSelectionnes: number[] = []; // IDs cochés pour "plusieurs parents"
  sujet = '';
  contenu = '';
  type: 'ANNONCE' | 'RAPPEL' | 'ANNULATION' = 'ANNONCE';

  // État UI
  enCours = false;
  messageRetour = '';
  erreur = '';

  constructor(
    private messageService: MessageService,
    private userService: UserService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // Charger parents et animateurs
    this.userService.getUsersByRole('PARENT').subscribe({
      next: (data) => { this.parents = data; this.cdr.detectChanges(); },
      error: () => { this.erreur = 'Impossible de charger la liste des parents'; this.cdr.detectChanges(); },
    });
    this.userService.getUsersByRole('ANIMATEUR').subscribe({
      next: (data) => { this.animateurs = data; this.cdr.detectChanges(); },
      error: () => {},
    });
  }

  changerCible(): void {
    this.recipientId = null;
    this.parentIdsSelectionnes = [];
  }

  // Gestion des cases à cocher pour "Plusieurs parents"
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

    if ((this.cible === 'parent' || this.cible === 'animateur') && !this.recipientId) {
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
        this.messageService.envoyerATousParents(this.sujet, this.contenu, this.type)
          .subscribe({ next: ok, error: fail });
        break;
      case 'tous-animateurs':
        this.messageService.envoyerATousAnimateurs(this.sujet, this.contenu, this.type)
          .subscribe({ next: ok, error: fail });
        break;
      case 'parent':
        // Envoi ciblé par ID parent
        this.messageService.envoyerAUnParent(this.recipientId!, this.sujet, this.contenu, this.type)
          .subscribe({ next: ok, error: fail });
        break;
      case 'animateur':
        // Envoi ciblé par ID animateur
        this.messageService.envoyerAUnAnimateur(this.recipientId!, this.sujet, this.contenu, this.type)
          .subscribe({ next: ok, error: fail });
        break;
      case 'plusieurs-parents':
        // Envoi à plusieurs parents sélectionnés via cases à cocher
        this.messageService.envoyerAPlusieursParents(this.parentIdsSelectionnes, this.sujet, this.contenu, this.type)
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
