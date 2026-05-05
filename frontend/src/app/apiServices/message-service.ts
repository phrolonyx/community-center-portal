import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { User } from '../models/user.model';

// Body envoyé à POST /admin/messages et /animateur/messages
export interface MessageRequest {
  sujet: string;
  contenu: string;
  recipientId?: number | null;
  recipientIds?: number[];
  type?: string;
  cible:
    | 'tous-parents'
    | 'tous-animateurs'
    | 'tous-admins'
    | 'parent'
    | 'animateur'
    | 'admin'
    | 'plusieurs-parents';
}

// Message reçu (boîte de réception parent / admin / animateur)
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

// Message dans l'historique des envois (regroupé si multi-destinataires)
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

@Injectable({ providedIn: 'root' })
export class MessageService {

  private readonly adminUrl = `${environment.apiUrl}/admin/messages`;
  private readonly adminInboxUrl = `${environment.apiUrl}/admin/inbox`;
  private readonly parentUrl = `${environment.apiUrl}/parent/messages`;
  private readonly animateurUrl = `${environment.apiUrl}/animateur/messages`;
  private readonly animateurInboxUrl = `${environment.apiUrl}/animateur/inbox`;

  constructor(private http: HttpClient) {}

  // ========================================================================
  // ADMIN — Envoi
  // ========================================================================

  envoyerATousParents(sujet: string, contenu: string, type = 'ANNONCE') {
    return this.postMessage(this.adminUrl, { sujet, contenu, type, cible: 'tous-parents' });
  }

  envoyerATousAnimateurs(sujet: string, contenu: string, type = 'ANNONCE') {
    return this.postMessage(this.adminUrl, { sujet, contenu, type, cible: 'tous-animateurs' });
  }

  envoyerAUnParent(parentId: number, sujet: string, contenu: string, type = 'ANNONCE') {
    return this.postMessage(this.adminUrl, { sujet, contenu, recipientId: parentId, type, cible: 'parent' });
  }

  envoyerAUnAnimateur(animateurId: number, sujet: string, contenu: string, type = 'ANNONCE') {
    return this.postMessage(this.adminUrl, { sujet, contenu, recipientId: animateurId, type, cible: 'animateur' });
  }

  envoyerAPlusieursParents(parentIds: number[], sujet: string, contenu: string, type = 'ANNONCE') {
    return this.postMessage(this.adminUrl, { sujet, contenu, recipientIds: parentIds, type, cible: 'plusieurs-parents' });
  }

  getMessagesEnvoyesAdmin(): Observable<SentMessageDto[]> {
    return this.http.get<SentMessageDto[]>(`${this.adminUrl}/sent`, { withCredentials: true });
  }

  // ========================================================================
  // ADMIN — Boîte de réception
  // ========================================================================

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

  // ========================================================================
  // ANIMATEUR — Envoi
  // ========================================================================

  envoyerATousParentsAnim(sujet: string, contenu: string, type = 'ANNONCE') {
    return this.postMessage(this.animateurUrl, { sujet, contenu, type, cible: 'tous-parents' });
  }

  envoyerATousAdminsAnim(sujet: string, contenu: string, type = 'ANNONCE') {
    return this.postMessage(this.animateurUrl, { sujet, contenu, type, cible: 'tous-admins' });
  }

  envoyerAUnParentAnim(parentId: number, sujet: string, contenu: string, type = 'ANNONCE') {
    return this.postMessage(this.animateurUrl, { sujet, contenu, recipientId: parentId, type, cible: 'parent' });
  }

  envoyerAUnAdminAnim(adminId: number, sujet: string, contenu: string, type = 'ANNONCE') {
    return this.postMessage(this.animateurUrl, { sujet, contenu, recipientId: adminId, type, cible: 'admin' });
  }

  envoyerAPlusieursParentsAnim(parentIds: number[], sujet: string, contenu: string, type = 'ANNONCE') {
    return this.postMessage(this.animateurUrl, { sujet, contenu, recipientIds: parentIds, type, cible: 'plusieurs-parents' });
  }

  getMessagesEnvoyesAnim(): Observable<SentMessageDto[]> {
    return this.http.get<SentMessageDto[]>(`${this.animateurUrl}/sent`, { withCredentials: true });
  }

  // Listes de destinataires pour les selects côté animateur
  getParentsForAnim(): Observable<User[]> {
    return this.http.get<User[]>(`${this.animateurUrl}/parents`, { withCredentials: true });
  }

  getAdminsForAnim(): Observable<User[]> {
    return this.http.get<User[]>(`${this.animateurUrl}/admins`, { withCredentials: true });
  }

  // ========================================================================
  // ANIMATEUR — Boîte de réception
  // ========================================================================

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

  // ========================================================================
  // PARENT — Boîte de réception
  // ========================================================================

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

  // ========================================================================
  // Helper privé : POST avec body MessageRequest
  // ========================================================================

  private postMessage(url: string, body: MessageRequest): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(url, body, { withCredentials: true });
  }
}
