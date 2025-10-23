import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { BeneficioService } from './beneficio.service';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <section>
    <h2>Transferir</h2>
    <form (ngSubmit)="onSubmit()" #f="ngForm" class="form">
      <label>
        De (ID)
        <input name="fromId" type="number" [(ngModel)]="fromId" required />
      </label>
      <label>
        Para (ID)
        <input name="toId" type="number" [(ngModel)]="toId" required />
      </label>
      <label>
        Valor
        <input name="amount" type="number" step="0.01" [(ngModel)]="amount" required />
      </label>
      <div class="actions">
        <button class="btn" type="submit" [disabled]="f.invalid">Transferir</button>
      </div>
    </form>
  </section>
  `
})
export class TransferComponent {
  fromId: number | null = null;
  toId: number | null = null;
  amount: number | null = null;

  constructor(private api: BeneficioService, private router: Router) {}

  onSubmit() {
    if (this.fromId && this.toId && this.amount) {
      this.api.transfer(this.fromId, this.toId, this.amount).subscribe({
        next: _ => {
          alert('Transferência realizada com sucesso');
          this.router.navigateByUrl('/');
        },
        error: err => alert('Erro na transferência: ' + (err?.error || err?.message || err))
      });
    }
  }
}