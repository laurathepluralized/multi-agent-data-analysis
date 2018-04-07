import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SimulationRun } from './simulation-run.model';
import { SimulationRunPopupService } from './simulation-run-popup.service';
import { SimulationRunService } from './simulation-run.service';

@Component({
    selector: 'jhi-simulation-run-delete-dialog',
    templateUrl: './simulation-run-delete-dialog.component.html'
})
export class SimulationRunDeleteDialogComponent {

    simulationRun: SimulationRun;

    constructor(
        private simulationRunService: SimulationRunService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.simulationRunService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'simulationRunListModification',
                content: 'Deleted an simulationRun'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-simulation-run-delete-popup',
    template: ''
})
export class SimulationRunDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private simulationRunPopupService: SimulationRunPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.simulationRunPopupService
                .open(SimulationRunDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
