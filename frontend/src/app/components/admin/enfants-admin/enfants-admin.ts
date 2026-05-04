import {Component, computed, OnInit, signal} from '@angular/core';
import {ChildService} from '../../../apiServices/child-service';
import {Child} from '../../../models/child.model';

@Component({
  selector: 'app-enfants-admin',
  standalone: false,
  templateUrl: './enfants-admin.html',
  styleUrl: './enfants-admin.css',
})
export class EnfantsAdmin implements OnInit {

  constructor(private childService: ChildService) {}

  ngOnInit() {
    this.children()
  }

  childrens = signal<Child[]>([]);

  searchValue = signal('');

  children() {
    this.childService.getAllChildren().subscribe({
      next: data => this.childrens.set(data),
      error: error => this.childrens.set([])
    })
  }

  // Recherche API
  searchChildren = computed(() => {
    const search = this.searchValue().toLowerCase().trim();
    if (!search) return this.childrens();

    return this.childrens().filter(child =>
      child.firstName.toLowerCase().includes(search) ||
      child.lastName.toLowerCase().includes(search)
    );
  });
    // Recherche
  // searchValue = "";

  // Liste des enfants (à remplacer par API)
  // children = [
    // Exemple :
    // {
    //   id: 1,
    //   fullName: "Emma Dupont",
    //   dateOfBirth: new Date(2020, 4, 12),
    //   age: 4,
    //   parentLinks: [
    //     { parentName: "Jean Dupont" },
    //     { parentName: "Marie Dupont" }
    //   ],
    //   medicalConditions: ["Asthme"]
    // }
  // ];

  // Recherche API
  // searchChildren() {
  //   console.log("Recherche :", this.searchValue);
  //
  // }

// Supprimer enfant
  deleteChild(id: number) {
    console.log("Supprimer enfant ID:", id);

    this.childService.deleteChild(id).subscribe({
      next: () => {
        // Mise à jour locale
        const updated = this.childrens().filter(c => c.id !== id);
        this.childrens.set(updated);
      },
      error: err => console.error("Erreur suppression enfant :", err)
    });
  }
}
