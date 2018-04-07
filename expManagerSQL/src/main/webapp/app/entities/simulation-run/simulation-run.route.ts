import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { SimulationRunComponent } from './simulation-run.component';
import { SimulationRunDetailComponent } from './simulation-run-detail.component';
import { SimulationRunPopupComponent } from './simulation-run-dialog.component';
import { SimulationRunDeletePopupComponent } from './simulation-run-delete-dialog.component';

@Injectable()
export class SimulationRunResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const simulationRunRoute: Routes = [
    {
        path: 'simulation-run',
        component: SimulationRunComponent,
        resolve: {
            'pagingParams': SimulationRunResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SimulationRuns'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'simulation-run/:id',
        component: SimulationRunDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SimulationRuns'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const simulationRunPopupRoute: Routes = [
    {
        path: 'simulation-run-new',
        component: SimulationRunPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SimulationRuns'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'simulation-run/:id/edit',
        component: SimulationRunPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SimulationRuns'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'simulation-run/:id/delete',
        component: SimulationRunDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SimulationRuns'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
