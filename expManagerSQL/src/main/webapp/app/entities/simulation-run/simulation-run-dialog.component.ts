import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SimulationRun } from './simulation-run.model';
import { SimulationRunPopupService } from './simulation-run-popup.service';
import { SimulationRunService } from './simulation-run.service';

@Component({
    selector: 'jhi-simulation-run-dialog',
    templateUrl: './simulation-run-dialog.component.html'
})
export class SimulationRunDialogComponent implements OnInit {

    simulationRun: SimulationRun;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private simulationRunService: SimulationRunService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.simulationRun.id !== undefined) {
            this.subscribeToSaveResponse(
                this.simulationRunService.update(this.simulationRun));
        } else {
            this.subscribeToSaveResponse(
                this.simulationRunService.create(this.simulationRun));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<SimulationRun>>) {
        result.subscribe((res: HttpResponse<SimulationRun>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: SimulationRun) {
        this.eventManager.broadcast({ name: 'simulationRunListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-simulation-run-popup',
    template: ''
})
export class SimulationRunPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private simulationRunPopupService: SimulationRunPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.simulationRunPopupService
                    .open(SimulationRunDialogComponent as Component, params['id']);
            } else {
                this.simulationRunPopupService
                    .open(SimulationRunDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
