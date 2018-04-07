/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ExpManagerSqlTestModule } from '../../../test.module';
import { SimulationRunDetailComponent } from '../../../../../../main/webapp/app/entities/simulation-run/simulation-run-detail.component';
import { SimulationRunService } from '../../../../../../main/webapp/app/entities/simulation-run/simulation-run.service';
import { SimulationRun } from '../../../../../../main/webapp/app/entities/simulation-run/simulation-run.model';

describe('Component Tests', () => {

    describe('SimulationRun Management Detail Component', () => {
        let comp: SimulationRunDetailComponent;
        let fixture: ComponentFixture<SimulationRunDetailComponent>;
        let service: SimulationRunService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ExpManagerSqlTestModule],
                declarations: [SimulationRunDetailComponent],
                providers: [
                    SimulationRunService
                ]
            })
            .overrideTemplate(SimulationRunDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SimulationRunDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SimulationRunService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new SimulationRun(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.simulationRun).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
