import {Component, OnInit, signal} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ActivityService} from '../../../apiServices/activity-service';
import {Activity} from '../../../models/activity.model';

@Component({
  selector: 'app-form-activite-admin',
  standalone: false,
  templateUrl: './form-activite-admin.html',
  styleUrl: './form-activite-admin.css',
})
export class FormActiviteAdmin implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private activityService: ActivityService
  ) {}

  // Loader
  loading = signal(true);

  // Titre dynamique
  formTitle = signal("Nouvelle activité");

  // Activité chargée
  activity = signal<Activity>({
    id: -1,
    name: "",
    description: "",
    activityImagePath: "",
    imageFile: undefined,
    sessions: []
  });

  // FORMULAIRE SIMPLE (compatible ngModel)
  form = {
    name: "",
    description: "",
    imageFile: undefined as File | undefined
  };

  selectedFileName = signal("");

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get("id");

    if (id) {
      this.formTitle.set("Modifier une activité");
      this.loadActivity(+id);
    } else {
      this.loading.set(false);
    }
  }

  loadActivity(id: number) {
    this.activityService.getActivityById(id).subscribe({
      next: data => {
        this.activity.set(data);

        // Remplir le formulaire
        this.form.name = data.name;
        this.form.description = data.description ?? "";

        this.loading.set(false);
      },
      error: err => console.error("Erreur chargement activité :", err)
    });
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    this.form.imageFile = file;
    this.selectedFileName.set(file.name);
  }

  saveActivity() {
    const a = this.activity();

    const body = {
      name: this.form.name,
      description: this.form.description
    };

    const request = a.id !== -1
      ? this.activityService.updateActivityJson(a.id, body)
      : this.activityService.createActivityJson(body);

    request.subscribe({
      next: saved => {

        if (!this.form.imageFile) {
          this.router.navigate(['/admin/activites']);
          return;
        }

        this.activityService.uploadImage(saved.id, this.form.imageFile).subscribe({
          next: () => this.router.navigate(['/admin/activites']),
          error: err => console.error("Erreur upload image :", err)
        });
      },
      error: err => console.error("Erreur sauvegarde activité :", err)
    });
  }
}
