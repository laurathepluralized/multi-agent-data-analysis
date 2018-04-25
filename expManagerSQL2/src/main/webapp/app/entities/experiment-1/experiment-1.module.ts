import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ExpManagerSql2SharedModule } from '../../shared';
import {
    Experiment1Service,
    Experiment1PopupService,
    Experiment1Component,
    Experiment1DetailComponent,
    Experiment1DialogComponent,
    Experiment1PopupComponent,
    Experiment1DeletePopupComponent,
    Experiment1DeleteDialogComponent,
    experiment1Route,
    experiment1PopupRoute,
    Experiment1ResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...experiment1Route,
    ...experiment1PopupRoute,
];

@NgModule({
    imports: [
        ExpManagerSql2SharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        Experiment1Component,
        Experiment1DetailComponent,
        Experiment1DialogComponent,
        Experiment1DeleteDialogComponent,
        Experiment1PopupComponent,
        Experiment1DeletePopupComponent,
    ],
    entryComponents: [
        Experiment1Component,
        Experiment1DialogComponent,
        Experiment1PopupComponent,
        Experiment1DeleteDialogComponent,
        Experiment1DeletePopupComponent,
    ],
    providers: [
        Experiment1Service,
        Experiment1PopupService,
        Experiment1ResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ExpManagerSql2Experiment1Module {}
