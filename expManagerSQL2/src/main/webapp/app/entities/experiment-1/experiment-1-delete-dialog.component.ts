import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Experiment1 } from './experiment-1.model';
import { Experiment1PopupService } from './experiment-1-popup.service';
import { Experiment1Service } from './experiment-1.service';

@Component({
    selector: 'jhi-experiment-1-delete-dialog',
    templateUrl: './experiment-1-delete-dialog.component.html'
})
export class Experiment1DeleteDialogComponent {

    experiment1: Experiment1;

    constructor(
        private experiment1Service: Experiment1Service,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.experiment1Service.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'experiment1ListModification',
                content: 'Deleted an experiment1'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-experiment-1-delete-popup',
    template: ''
})
export class Experiment1DeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private experiment1PopupService: Experiment1PopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.experiment1PopupService
                .open(Experiment1DeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
