import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface MessageRequest {
  sujet: string;
  contenu: string;
  recipientId?: number | null;
  recipientIds?: number[]; // pour envoi à plusieurs parents
  type?: string;
  cible: 'tous-parents' | 'tous-animateurs' | 'tous-admins' | 'parent' | 'animateur' | 'admin' | 'plusieurs-parents';
}

export interface MessageDto {
  id: number;
  subject: string;
  content: string;
  isRead: boolean;
  type: string;
  senderInitials: string;
  sender: { fullName: string };
  createdAt: string;
}

export interface SentMessageDto {
  id: number;
  subject: string;
  content: string;
  type: string;
  createdAt: string;
  isRead: boolean;
  recipientName: string;
  recipientCount: number; // 0 = broadcast global, 1 = ciblé, N = envoi groupé
}

@Injectable({
  providedIn: 'root',
})
export class MessageService {

  private adminUrl = `${environment.apiUrl}/admin/messages`;
  private adminInboxUrl = `${environment.apiUrl}/admin/inbox`;
  private parentUrl = `${environment.apiUrl}/parent/messages`;
  private animateurUrl = `${environment.apiUrl}/animateur/messages`;
  private animateurInboxUrl = `${environment.apiUrl}/animateur/inbox`;

  constructor(private http: HttpClient) {}

  // === ENVOI ADMIN ===

  // Envoi groupé à tous les parents
  envoyerATousParents(sujet: string, contenu: string, type = 'ANNONCE'): Observable<{ message: string }> {
    const body: MessageRequest = { sujet, contenu, type, cible: 'tous-parents' };
    return this.http.post<{ message: string }>(this.adminUrl, body, { withCredentials: true });
  }

  // Envoi groupé à tous les animateurs
  envoyerATousAnimateurs(sujet: string, contenu: string, type = 'ANNONCE'): Observable<{ message: string }> {
    const body: MessageRequest = { sujet, contenu, type, cible: 'tous-animateurs' };
    return this.http.post<{ message: string }>(this.adminUrl, body, { withCredentials: true });
  }

  // Envoi ciblé à un parent par ID
  envoyerAUnParent(parentId: number, sujet: string, contenu: string, type = 'ANNONCE'): Observable<{ message: string }> {
    const body: MessageRequest = { sujet, contenu, recipientId: parentId, type, cible: 'parent' };
    return this.http.post<{ message: string }>(this.adminUrl, body, { withCredentials: true });
  }

  // Envoi ciblé à un animateur par ID
  envoyerAUnAnimateur(animateurId: number, sujet: string, contenu: string, type = 'ANNONCE'): Observable<{ message: string }> {
    const body: MessageRequest = { sujet, contenu, recipientId: animateurId, type, cible: 'animateur' };
    return this.http.post<{ message: string }>(this.adminUrl, body, { withCredentials: true });
  }

  // Envoi à plusieurs parents (cote admin)
  envoyerAPlusieursParents(parentIds: number[], sujet: string, contenu: string, type = 'ANNONCE'): Observable<{ message: string }> {
    const body: MessageRequest = { sujet, contenu, recipientIds: parentIds, type, cible: 'plusieurs-parents' };
    return this.http.post<{ message: string }>(this.adminUrl, body, { withCredentials: true });
  }

  // Historique des messages envoyés (cote admin)
  getMessagesEnvoyesAdmin(): Observable<SentMessageDto[]> {
    return this.http.get<SentMessageDto[]>(`${this.adminUrl}/sent`, { withCredentials: true });
  }

  // === BOITE PARENT ===
  getMesMessages(): Observable<MessageDto[]> {
    return this.http.get<MessageDto[]>(this.parentUrl, { withCredentials: true });
  }
  countUnread(): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.parentUrl}/unread-count`, { withCredentials: true });
  }
  marquerLu(messageId: number): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(`${this.parentUrl}/${messageId}/read`, {}, { withCredentials: true });
  }
  supprimer(messageId: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.parentUrl}/${messageId}`, { withCredentials: true });
  }

  // === BOITE ADMIN ===
  getMesMessagesAdmin(): Observable<MessageDto[]> {
    return this.http.get<MessageDto[]>(this.adminInboxUrl, { withCredentials: true });
  }
  countUnreadAdmin(): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.adminInboxUrl}/unread-count`, { withCredentials: true });
  }
  marquerLuAdmin(messageId: number): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(`${this.adminInboxUrl}/${messageId}/read`, {}, { withCredentials: true });
  }
  supprimerAdmin(messageId: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.adminInboxUrl}/${messageId}`, { withCredentials: true });
  }

  // === ENVOI ANIMATEUR ===
  envoyerATousParentsAnim(sujet: string, contenu: string, type = 'ANNONCE'): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(this.animateurUrl,
      { sujet, contenu, type, cible: 'tous-parents' }, { withCredentials: true });
  }
  envoyerATousAdminsAnim(sujet: string, contenu: string, type = 'ANNONCE'): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(this.animateurUrl,
      { sujet, contenu, type, cible: 'tous-admins' }, { withCredentials: true });
  }
  envoyerAUnParentAnim(parentId: number, sujet: string, contenu: string, type = 'ANNONCE'): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(this.animateurUrl,
      { sujet, contenu, recipientId: parentId, type, cible: 'parent' }, { withCredentials: true });
  }
  envoyerAUnAdminAnim(adminId: number, sujet: string, contenu: string, type = 'ANNONCE'): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(this.animateurUrl,
      { sujet, contenu, recipientId: adminId, type, cible: 'admin' }, { withCredentials: true });
  }

  // Envoi à plusieurs parents (cote animateur)
  envoyerAPlusieursParentsAnim(parentIds: number[], sujet: string, contenu: string, type = 'ANNONCE'): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(this.animateurUrl,
      { sujet, contenu, recipientIds: parentIds, type, cible: 'plusieurs-parents' }, { withCredentials: true });
  }

  // Historique des messages envoyés (cote animateur)
  getMessagesEnvoyesAnim(): Observable<SentMessageDto[]> {
    return this.http.get<SentMessageDto[]>(`${this.animateurUrl}/sent`, { withCredentials: true });
  }

  // Listes de destinataires (endpoints dediés animateur)
  getParentsForAnim(): Observable<any[]> {
    return this.http.get<any[]>(`${this.animateurUrl}/parents`, { withCredentials: true });
  }
  getAdminsForAnim(): Observable<any[]> {
    return this.http.get<any[]>(`${this.animateurUrl}/admins`, { withCredentials: true });
  }

  // === BOITE ANIMATEUR ===
  getMesMessagesAnim(): Observable<MessageDto[]> {
    return this.http.get<MessageDto[]>(this.animateurInboxUrl, { withCredentials: true });
  }
  countUnreadAnim(): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.animateurInboxUrl}/unread-count`, { withCredentials: true });
  }
  marquerLuAnim(messageId: number): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(`${this.animateurInboxUrl}/${messageId}/read`, {}, { withCredentials: true });
  }
  supprimerAnim(messageId: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.animateurInboxUrl}/${messageId}`, { withCredentials: true });
  }
}
