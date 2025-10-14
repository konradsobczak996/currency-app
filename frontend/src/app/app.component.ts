import { Component, OnInit } from '@angular/core';
import { CurrencyService, CurrencyRequestView, CurrencyValueResponse, CurrencyCodeView } from './currency.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  currency = '';
  name = '';
  result?: number;
  errorMsg = '';

  filterCode = '';
  history: any[] = [];
  loading = false;

  constructor(private api: CurrencyService) {}

  ngOnInit(): void { this.loadHistory(); }

  submit() {
    this.errorMsg = '';
    this.result = undefined;

    const code = (this.currency || '').trim().toUpperCase();
    if (!/^[A-Z]{3}$/.test(code)) { this.errorMsg = 'Podaj 3-literowy kod (np. EUR).'; return; }

    this.api.getCurrentValue({ currency: code, name: this.name })
      .subscribe({
        next: r => { this.result = r.value; this.loadHistory(); },
        error: e => {
          this.errorMsg =
            e?.status === 404 ? 'Nie znaleziono waluty w NBP.' :
            e?.status === 400 ? 'Błędne dane wejściowe.' :
            (e?.error?.detail || e?.message || 'Błąd zapytania');
        }
      });
  }

  loadHistory() {
    this.loading = true;
    const f = (this.filterCode || '').trim().toUpperCase();
    this.api.listRequests(f).subscribe({
      next: list => { this.history = list; this.loading = false; },
      error: e => { this.errorMsg = e?.message || 'Błąd pobierania historii'; this.loading = false; }
    });
  }
}
