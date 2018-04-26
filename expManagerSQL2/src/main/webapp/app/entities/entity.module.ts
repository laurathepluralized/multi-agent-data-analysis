import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ExpManagerSql2Experiment1Module } from './experiment-1/experiment-1.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ExpManagerSql2Experiment1Module,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ExpManagerSql2EntityModule {}
