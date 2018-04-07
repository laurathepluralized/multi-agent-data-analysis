import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { SimulationRun } from './simulation-run.model';
import { SimulationRunService } from './simulation-run.service';

@Component({
    selector: 'jhi-simulation-run-detail',
    templateUrl: './simulation-run-detail.component.html'
})
export class SimulationRunDetailComponent implements OnInit, OnDestroy {

    simulationRun: SimulationRun;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private simulationRunService: SimulationRunService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSimulationRuns();
    }

    load(id) {
        this.simulationRunService.find(id)
            .subscribe((simulationRunResponse: HttpResponse<SimulationRun>) => {
                this.simulationRun = simulationRunResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSimulationRuns() {
        this.eventSubscriber = this.eventManager.subscribe(
            'simulationRunListModification',
            (response) => this.load(this.simulationRun.id)
        );
    }
}
