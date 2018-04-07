import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ExpManagerSqlSharedModule } from '../../shared';
import {
    SimulationRunService,
    SimulationRunPopupService,
    SimulationRunComponent,
    SimulationRunDetailComponent,
    SimulationRunDialogComponent,
    SimulationRunPopupComponent,
    SimulationRunDeletePopupComponent,
    SimulationRunDeleteDialogComponent,
    simulationRunRoute,
    simulationRunPopupRoute,
    SimulationRunResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...simulationRunRoute,
    ...simulationRunPopupRoute,
];

@NgModule({
    imports: [
        ExpManagerSqlSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SimulationRunComponent,
        SimulationRunDetailComponent,
        SimulationRunDialogComponent,
        SimulationRunDeleteDialogComponent,
        SimulationRunPopupComponent,
        SimulationRunDeletePopupComponent,
    ],
    entryComponents: [
        SimulationRunComponent,
        SimulationRunDialogComponent,
        SimulationRunPopupComponent,
        SimulationRunDeleteDialogComponent,
        SimulationRunDeletePopupComponent,
    ],
    providers: [
        SimulationRunService,
        SimulationRunPopupService,
        SimulationRunResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ExpManagerSqlSimulationRunModule {}
