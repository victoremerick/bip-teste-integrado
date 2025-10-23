import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BeneficioService } from './beneficio.service';
import { Beneficio } from './types';

@Component({
  selector: 'app-beneficio-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <section>
    <h2>Lista de Benefícios</h2>
    <a class="btn" routerLink="/novo">+ Novo</a>
    <table class="table" *ngIf="items.length; else vazio">
      <thead>
        <tr>
          <th>ID</th>
          <th>Nome</th>
          <th>Descrição</th>
          <th>Valor</th>
          <th>Ativo</th>
          <th>Ações</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let b of items">
          <td>{{b.id}}</td>
          <td>{{b.nome}}</td>
          <td>{{b.descricao}}</td>
          <td>{{b.valor}}</td>
          <td>{{b.ativo ? 'Sim' : 'Não'}}</td>
          <td>
            <a class="btn" [routerLink]="['/editar', b.id]">Editar</a>
            <button class="btn danger" (click)="onDelete(b)">Excluir</button>
          </td>
        </tr>
      </tbody>
    </table>
    <ng-template #vazio>
      <p>Nenhum benefício cadastrado.</p>
    </ng-template>
  </section>
  `
})
export class BeneficioListComponent implements OnInit {
  items: Beneficio[] = [];
  loading = false;

  constructor(private api: BeneficioService) {}

  ngOnInit(): void {
    this.load();
  }

  load() {
    this.loading = true;
    this.api.list().subscribe({
      next: data => this.items = data,
      error: err => alert('Erro ao carregar lista: ' + (err?.error || err?.message || err)),
      complete: () => this.loading = false
    });
  }

  onDelete(b: Beneficio) {
    if (!b.id) return;
    if (!confirm(`Excluir benefício ${b.nome}?`)) return;
    this.api.delete(b.id).subscribe({
      next: () => this.load(),
      error: err => alert('Erro ao excluir: ' + (err?.error || err?.message || err))
    });
  }
}