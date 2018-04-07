import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { SimulationRun } from './simulation-run.model';
import { SimulationRunService } from './simulation-run.service';

@Injectable()
export class SimulationRunPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private simulationRunService: SimulationRunService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.simulationRunService.find(id)
                    .subscribe((simulationRunResponse: HttpResponse<SimulationRun>) => {
                        const simulationRun: SimulationRun = simulationRunResponse.body;
                        this.ngbModalRef = this.simulationRunModalRef(component, simulationRun);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.simulationRunModalRef(component, new SimulationRun());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    simulationRunModalRef(component: Component, simulationRun: SimulationRun): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.simulationRun = simulationRun;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
