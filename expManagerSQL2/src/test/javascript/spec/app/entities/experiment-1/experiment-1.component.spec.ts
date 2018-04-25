/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ExpManagerSql2TestModule } from '../../../test.module';
import { Experiment1Component } from '../../../../../../main/webapp/app/entities/experiment-1/experiment-1.component';
import { Experiment1Service } from '../../../../../../main/webapp/app/entities/experiment-1/experiment-1.service';
import { Experiment1 } from '../../../../../../main/webapp/app/entities/experiment-1/experiment-1.model';

describe('Component Tests', () => {

    describe('Experiment1 Management Component', () => {
        let comp: Experiment1Component;
        let fixture: ComponentFixture<Experiment1Component>;
        let service: Experiment1Service;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ExpManagerSql2TestModule],
                declarations: [Experiment1Component],
                providers: [
                    Experiment1Service
                ]
            })
            .overrideTemplate(Experiment1Component, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(Experiment1Component);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(Experiment1Service);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Experiment1(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.experiment1S[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
