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

  // Titre dynamique
  formTitle = signal("Nouvelle activité");

  // Signal pour l'activité (sert à savoir si on est en édition)
  activity = signal<Activity>({
    id: -1,
    name: "",
    description: "",
    activityImagePath: "",
    imageFile: undefined
  });

  // Objet simple pour le formulaire (compatible ngModel)
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
    }
  }

  // Charger activité si édition
  loadActivity(id: number) {
    this.activityService.getActivityById(id).subscribe({
      next: data => {
        this.activity.set(data);

        // Remplir le formulaire
        this.form.name = data.name;
        this.form.description = data.description ?? "";
      },
      error: err => console.error("Erreur chargement activité :", err)
    });
  }

  // Upload image
  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    this.form.imageFile = file;
    this.selectedFileName.set(file.name);
  }

  // Sauvegarde activité
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
      next: (savedActivity) => {

        // Si pas d'image → terminé
        if (!this.form.imageFile) {
          this.router.navigate(['/admin/activites']);
          return;
        }

        // Sinon upload image séparément
        const fd = new FormData();
        fd.append("imageFile", this.form.imageFile);

        this.activityService.uploadImage(savedActivity.id, fd).subscribe({
          next: () => this.router.navigate(['/admin/activites']),
          error: err => console.error("Erreur upload image :", err)
        });
      },
      error: err => console.error("Erreur sauvegarde activité :", err)
    });
  }
}
// Titre dynamique (ajout / édition)
//   formTitle = "Nouvelle activité";
  // À remplacer par un titre dynamique selon route param

  // Objet activité (à remplacer par API)
  // activity = {
  //   id: null,
  //   name: "",
  //   description: "",
  //   imageFile: null
  // };

  // selectedFileName = "";

  // Upload image
  // onFileSelected(event: any) {
  //   const file = event.target.files[0];
  //   this.activity.imageFile = file;
  //   this.selectedFileName = file ? file.name : "";
  // }

  // Sauvegarde activité
  // saveActivity() {
  //   console.log("Données envoyées :", this.activity);

    //
    // const formData = new FormData();
    // formData.append("id", this.activity.id);
    // formData.append("name", this.activity.name);
    // formData.append("description", this.activity.description);
    // if (this.activity.imageFile) {
    //   formData.append("imageFile", this.activity.imageFile);
    // }

    //
//   }
// }
