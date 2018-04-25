import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Experiment1 } from './experiment-1.model';
import { Experiment1Service } from './experiment-1.service';

@Component({
    selector: 'jhi-experiment-1-detail',
    templateUrl: './experiment-1-detail.component.html'
})
export class Experiment1DetailComponent implements OnInit, OnDestroy {

    experiment1: Experiment1;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private experiment1Service: Experiment1Service,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInExperiment1S();
    }

    load(id) {
        this.experiment1Service.find(id)
            .subscribe((experiment1Response: HttpResponse<Experiment1>) => {
                this.experiment1 = experiment1Response.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInExperiment1S() {
        this.eventSubscriber = this.eventManager.subscribe(
            'experiment1ListModification',
            (response) => this.load(this.experiment1.id)
        );
    }
}
