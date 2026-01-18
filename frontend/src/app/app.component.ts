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
  page = 0;
  pageSize = 10;
  hasNext = false;

  constructor(private api: CurrencyService) {}

 ngOnInit(): void {
   this.loadPage(0);
 }

 applyFilter() {
   // po zmianie filtra zawsze wracamy na pierwszą stronę
   console.log("Test");
   this.loadPage(0);
 }

 submit() {
   this.errorMsg = '';
   this.result = undefined;

   const code = (this.currency || '').trim().toUpperCase();
   if (!/^[A-Z]{3}$/.test(code)) {
     this.errorMsg = 'Podaj 3-literowy kod (np. EUR).';
     return;
   }

   this.api.getCurrentValue({ currency: code, name: this.name }).subscribe({
     next: r => {
       this.result = r.value;
       // odśwież bieżącą stronę z aktualnym filtrem
       this.loadPage(this.page);
     },
     error: e => {
       this.errorMsg =
         e?.status === 404 ? 'Nie znaleziono waluty w NBP.' :
         e?.status === 400 ? 'Błędne dane wejściowe.' :
         (e?.error?.detail || e?.message || 'Błąd zapytania');
     }
   });
 }


 loadPage(pageIndex: number) {
   this.loading = true;
   this.errorMsg = '';
   this.hasNext = false;

   const f = (this.filterCode || '').trim().toUpperCase();
   const pageParam = String(pageIndex);

   this.api.listRequestsPage(f, pageParam).subscribe({
     next: list => {
       this.page = pageIndex;
       this.history = list;
       this.hasNext = (list?.length ?? 0) === this.pageSize;
       this.loading = false;
     },
     error: e => {
       this.errorMsg = e?.message || 'Błąd pobierania historii';
       this.loading = false;
     }
   });
 }

  nextPage() {
    if (!this.hasNext || this.loading) return;
    this.loadPage(this.page + 1);
  }

  previousPage() {
    if (this.page <= 0 || this.loading) return;
    this.loadPage(this.page - 1);
  }
}
