import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FlowbiteService {
  loadFlowbite(callback: (flowbite: any) => void) {
    import('flowbite').then(flowbite => {
      callback(flowbite);
    });
  }
}

