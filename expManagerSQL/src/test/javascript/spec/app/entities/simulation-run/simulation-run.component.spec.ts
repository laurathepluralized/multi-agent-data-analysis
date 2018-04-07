/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ExpManagerSqlTestModule } from '../../../test.module';
import { SimulationRunComponent } from '../../../../../../main/webapp/app/entities/simulation-run/simulation-run.component';
import { SimulationRunService } from '../../../../../../main/webapp/app/entities/simulation-run/simulation-run.service';
import { SimulationRun } from '../../../../../../main/webapp/app/entities/simulation-run/simulation-run.model';

describe('Component Tests', () => {

    describe('SimulationRun Management Component', () => {
        let comp: SimulationRunComponent;
        let fixture: ComponentFixture<SimulationRunComponent>;
        let service: SimulationRunService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ExpManagerSqlTestModule],
                declarations: [SimulationRunComponent],
                providers: [
                    SimulationRunService
                ]
            })
            .overrideTemplate(SimulationRunComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SimulationRunComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SimulationRunService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new SimulationRun(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.simulationRuns[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
