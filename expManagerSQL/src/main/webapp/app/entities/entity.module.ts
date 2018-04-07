import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ExpManagerSqlSimulationRunModule } from './simulation-run/simulation-run.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ExpManagerSqlSimulationRunModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ExpManagerSqlEntityModule {}
