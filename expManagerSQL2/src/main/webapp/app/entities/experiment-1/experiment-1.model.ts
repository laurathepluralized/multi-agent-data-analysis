import { BaseEntity } from './../../shared';

export class Experiment1 implements BaseEntity {
    constructor(
        public id?: number,
        public index?: string,
    ) {
    }
}
