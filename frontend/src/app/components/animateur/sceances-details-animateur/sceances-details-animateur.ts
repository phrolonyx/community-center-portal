import { Component, OnInit, signal } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { ActivitySessionService, ActivitySession } from '../../../apiServices/activity-session-service';
import {AuthService} from '../../../apiServices/auth.service';

@Component({
  selector: 'app-sceances-details-animateur',
  standalone: false,
  templateUrl: './sceances-details-animateur.html',
  styleUrl: './sceances-details-animateur.css',
})
export class SceancesDetailsAnimateur implements OnInit {

  session = signal<ActivitySession | null>(null);

  constructor(
    private route: ActivatedRoute,
    private sessionService: ActivitySessionService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.sessionService.getSessionById(id).subscribe({
      next: data => {
        this.session.set(data);

        const currentUser = this.authService.currentUser();

        if (data.hostId !== currentUser?.id) {
          this.router.navigate(['/animateur/sceances']);
        }
      },
      error: err => console.error(err)
    });
  }
}
