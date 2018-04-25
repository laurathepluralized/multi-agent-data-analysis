import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Experiment1 } from './experiment-1.model';
import { Experiment1PopupService } from './experiment-1-popup.service';
import { Experiment1Service } from './experiment-1.service';

@Component({
    selector: 'jhi-experiment-1-dialog',
    templateUrl: './experiment-1-dialog.component.html'
})
export class Experiment1DialogComponent implements OnInit {

    experiment1: Experiment1;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private experiment1Service: Experiment1Service,
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
        if (this.experiment1.id !== undefined) {
            this.subscribeToSaveResponse(
                this.experiment1Service.update(this.experiment1));
        } else {
            this.subscribeToSaveResponse(
                this.experiment1Service.create(this.experiment1));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Experiment1>>) {
        result.subscribe((res: HttpResponse<Experiment1>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Experiment1) {
        this.eventManager.broadcast({ name: 'experiment1ListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-experiment-1-popup',
    template: ''
})
export class Experiment1PopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private experiment1PopupService: Experiment1PopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.experiment1PopupService
                    .open(Experiment1DialogComponent as Component, params['id']);
            } else {
                this.experiment1PopupService
                    .open(Experiment1DialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
