import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BeneficioService } from './beneficio.service';
import { Beneficio } from './types';

@Component({
  selector: 'app-beneficio-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <section>
    <h2>{{editing ? 'Editar' : 'Novo'}} Benefício</h2>

    <form (ngSubmit)="onSubmit()" #f="ngForm" class="form">
      <label>
        Nome
        <input name="nome" [(ngModel)]="model.nome" required />
      </label>

      <label>
        Descrição
        <input name="descricao" [(ngModel)]="model.descricao" />
      </label>

      <label>
        Valor
        <input name="valor" type="number" step="0.01" [(ngModel)]="model.valor" required />
      </label>

      <label class="checkbox">
        <input type="checkbox" name="ativo" [(ngModel)]="model.ativo" /> Ativo
      </label>

      <div class="actions">
        <button class="btn" type="submit" [disabled]="f.invalid">Salvar</button>
        <button class="btn secondary" type="button" (click)="goBack()">Cancelar</button>
      </div>
    </form>
  </section>
  `
})
export class BeneficioFormComponent implements OnInit {
  model: Beneficio = { nome: '', descricao: '', valor: 0, ativo: true };
  editing = false;

  constructor(private api: BeneficioService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.editing = true;
      this.api.get(id).subscribe({
        next: b => this.model = b,
        error: err => alert('Erro ao carregar: ' + (err?.error || err?.message || err))
      });
    }
  }

  onSubmit() {
    if (this.editing && this.model.id) {
      this.api.update(this.model.id, this.model).subscribe({
        next: _ => this.router.navigateByUrl('/'),
        error: err => alert('Erro ao atualizar: ' + (err?.error || err?.message || err))
      });
    } else {
      this.api.create(this.model).subscribe({
        next: _ => this.router.navigateByUrl('/'),
        error: err => alert('Erro ao criar: ' + (err?.error || err?.message || err))
      });
    }
  }

  goBack() { this.router.navigateByUrl('/'); }
}