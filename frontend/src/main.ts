import { bootstrapApplication } from '@angular/platform-browser';
import { Component } from '@angular/core';
import { provideRouter, RouterOutlet, RouterLink, Routes } from '@angular/router';
import { HttpClientModule, provideHttpClient, withFetch } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BeneficioListComponent } from './app/list.component';
import { BeneficioFormComponent } from './app/form.component';
import { TransferComponent } from './app/transfer.component';

const routes: Routes = [
  { path: '', component: BeneficioListComponent },
  { path: 'novo', component: BeneficioFormComponent },
  { path: 'editar/:id', component: BeneficioFormComponent },
  { path: 'transferir', component: TransferComponent },
];

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, CommonModule],
  template: `
  <header class="topbar">
    <h1>Benefícios</h1>
    <nav>
      <a routerLink="/">Lista</a>
      <a routerLink="/novo">Novo</a>
      <a routerLink="/transferir">Transferir</a>
    </nav>
  </header>
  <main class="container">
    <router-outlet></router-outlet>
  </main>
  `
})
export class AppComponent {}

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideHttpClient(withFetch()),
    provideAnimations(),
    { provide: 'API_BASE', useValue: 'http://localhost:8080/api/v1/beneficios' }
  ]
}).catch(err => console.error(err));