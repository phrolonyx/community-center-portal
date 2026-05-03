import { Component } from '@angular/core';

@Component({
  selector: 'app-form-activite-admin',
  standalone: false,
  templateUrl: './form-activite-admin.html',
  styleUrl: './form-activite-admin.css',
})
export class FormActiviteAdmin {
// Titre dynamique (ajout / édition)
  formTitle = "Nouvelle activité"; 
  // À remplacer par un titre dynamique selon route param

  // Objet activité (à remplacer par API)
  activity = {
    id: null,
    name: "",
    description: "",
    imageFile: null
  };

  selectedFileName = "";

  // Upload image
  onFileSelected(event: any) {
    const file = event.target.files[0];
    this.activity.imageFile = file;
    this.selectedFileName = file ? file.name : "";
  }

  // Sauvegarde activité
  saveActivity() {
    console.log("Données envoyées :", this.activity);

    // TODO: envoyer FormData à ton API Spring Boot
    // const formData = new FormData();
    // formData.append("id", this.activity.id);
    // formData.append("name", this.activity.name);
    // formData.append("description", this.activity.description);
    // if (this.activity.imageFile) {
    //   formData.append("imageFile", this.activity.imageFile);
    // }

    // TODO: POST vers /admin/activities/save
  }
}
