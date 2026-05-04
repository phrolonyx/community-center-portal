import {ChangeDetectionStrategy, Component, OnInit, signal} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ActivitySessionService} from '../../../apiServices/activity-session-service';
import {ActivityService} from '../../../apiServices/activity-service';
import {UserService} from '../../../apiServices/user-service';
import {LocationService} from '../../../apiServices/location-service';

@Component({
  selector: 'app-form-sceance-admin',
  standalone: false,
  templateUrl: './form-sceance-admin.html',
  styleUrl: './form-sceance-admin.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FormSceanceAdmin implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private sessionService: ActivitySessionService,
    private activityService: ActivityService,
    private userService: UserService,
    private locationService: LocationService
  ) {}

  // Titre dynamique
  formTitle = signal("Nouvelle séance");

  // Objet simple pour le formulaire
  form = {
    id: -1,
    activityId: 0,
    hostId: 0,
    locationId: 0,
    capacity: 1,
    minAge: 0,
    maxAge: 0,
    dateTimeStart: "",
    dateTimeEnd: ""
  };

  // Listes dynamiques
  activities: any[] = [];
  hosts: any[] = [];
  locations: any[] = [];

  ngOnInit() {
    this.loadLists();

    const id = this.route.snapshot.paramMap.get("id");
    if (id) {
      this.formTitle.set("Modifier une séance");
      this.loadSession(+id);
    }
  }

  // Charger les listes (activités, animateurs, lieux)
  loadLists() {
    this.activityService.getActivities().subscribe({
      next: data => this.activities = data
    });

    this.userService.getUsersByRole('ANIMATEUR').subscribe({
      next: data => this.hosts = data
    });

    this.locationService.getAllLocations().subscribe({
      next: data => this.locations = data
    });
  }

  // Charger séance si édition
  loadSession(id: number) {
    this.sessionService.getAllSessions().subscribe({
      next: sessions => {
        const s = sessions.find(x => x.id === id);
        if (!s) return;

        this.form = {
          id: s.id,
          activityId: s.activityId,
          hostId: s.hostId,
          locationId: s.locationId,
          capacity: s.capacity,
          minAge: s.minAge,
          maxAge: s.maxAge,
          dateTimeStart: s.dateTimeStart,
          dateTimeEnd: s.dateTimeEnd
        };
      }
    });
  }

  // Sauvegarde séance
  saveSession() {
    const body = {
      activityId: this.form.activityId,
      hostId: this.form.hostId,
      locationId: this.form.locationId,
      capacity: this.form.capacity,
      minAge: this.form.minAge ?? 0,
      maxAge: this.form.maxAge ?? 0,
      dateTimeStart: this.form.dateTimeStart,
      dateTimeEnd: this.form.dateTimeEnd
    };

    const request = this.form.id !== -1
      ? this.sessionService.updateSession(this.form.id, body)
      : this.sessionService.createSession(body);

    request.subscribe({
      next: () => this.router.navigate(['/admin/sceances']),
      error: err => console.error("Erreur sauvegarde séance :", err)
    });
  }
}
