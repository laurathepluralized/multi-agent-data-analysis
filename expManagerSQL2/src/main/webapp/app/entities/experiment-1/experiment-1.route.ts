import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { Experiment1Component } from './experiment-1.component';
import { Experiment1DetailComponent } from './experiment-1-detail.component';
import { Experiment1PopupComponent } from './experiment-1-dialog.component';
import { Experiment1DeletePopupComponent } from './experiment-1-delete-dialog.component';

@Injectable()
export class Experiment1ResolvePagingParams implements Resolve<any> {

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

export const experiment1Route: Routes = [
    {
        path: 'experiment-1',
        component: Experiment1Component,
        resolve: {
            'pagingParams': Experiment1ResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiment1S'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'experiment-1/:id',
        component: Experiment1DetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiment1S'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const experiment1PopupRoute: Routes = [
    {
        path: 'experiment-1-new',
        component: Experiment1PopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiment1S'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'experiment-1/:id/edit',
        component: Experiment1PopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiment1S'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'experiment-1/:id/delete',
        component: Experiment1DeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiment1S'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
