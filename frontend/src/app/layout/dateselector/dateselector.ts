import { formatDate } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, inject, signal } from '@angular/core';
import { Datepicker } from 'flowbite';
import type { DatepickerOptions } from 'flowbite';
import type { InstanceOptions } from 'flowbite';

@Component({
  selector: 'app-dateselector',
  imports: [],
  templateUrl: './dateselector.html',
  styleUrl: './dateselector.css',
})
export class Dateselector implements AfterViewInit {

  datepicker!: Datepicker;
  $datepickerEl: HTMLInputElement;
  httpClient = inject(HttpClient);
  dates = signal<string[]>([]);

  ngAfterViewInit(): void {

    this.$datepickerEl = document.getElementById('date-range-picker') as HTMLInputElement;

    const options: DatepickerOptions = {
      defaultDatepickerId: null,
      autohide: false,
      format: 'dd-mm-yyyy',
      maxDate: null,
      minDate: null,
      orientation: 'bottom',
      buttons: false,
      autoSelectToday: 0,
      title: null,
      rangePicker: true,
      onShow: () => { },
      onHide: () => { },
    };

    const instanceOptions: InstanceOptions = {
      id: 'date-range-picker',
      override: true
    };

    this.datepicker = new Datepicker(
      this.$datepickerEl,
      options,
      instanceOptions
    );

    (this.datepicker.getDatepickerInstance() as any).allowOneSidedRange = true;

  }

  /**
   * Con la opción rangePicker: true, Flowbite gestiona los dos inputs como un único rango
   * y getDate() devuelve un array con las dos fechas [inicio, fin]
   * Flowbite declara que getDate() devuelve string | string[], aunque en ejecución devuelve objetos Date
   * Con as string[] solo le indicamos a TypeScript que use la variante de array; no convierte nada
   * Con formatDate se convierte cada fecha a texto 'yyyy-MM-dd' usando la hora local antes de enviarla
   * Sin esa conversión, al pasar un Date a HttpParams se usaría toString(), que genera un texto del estilo
   * "Wed Nov 04 2026 00:00:00 GMT+0100 (hora estándar…)" que el backend no puede parsear a LocalDate (error 400)
   */
  search() {
    this.dates.set(this.datepicker.getDate() as string[]);
    this.dates.update(dates => dates.map(dates => dates ? formatDate(dates, 'yyyy-MM-dd', 'es-ES') : null ));
  }

  restart() {
    const rangePicker = this.datepicker.getDatepickerInstance() as any;
    rangePicker.setDates({ clear: true }, { clear: true });
    this.dates.set([]);
  }

}


