/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ExpManagerSql2TestModule } from '../../../test.module';
import { Experiment1DetailComponent } from '../../../../../../main/webapp/app/entities/experiment-1/experiment-1-detail.component';
import { Experiment1Service } from '../../../../../../main/webapp/app/entities/experiment-1/experiment-1.service';
import { Experiment1 } from '../../../../../../main/webapp/app/entities/experiment-1/experiment-1.model';

describe('Component Tests', () => {

    describe('Experiment1 Management Detail Component', () => {
        let comp: Experiment1DetailComponent;
        let fixture: ComponentFixture<Experiment1DetailComponent>;
        let service: Experiment1Service;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ExpManagerSql2TestModule],
                declarations: [Experiment1DetailComponent],
                providers: [
                    Experiment1Service
                ]
            })
            .overrideTemplate(Experiment1DetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(Experiment1DetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(Experiment1Service);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Experiment1(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.experiment1).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
