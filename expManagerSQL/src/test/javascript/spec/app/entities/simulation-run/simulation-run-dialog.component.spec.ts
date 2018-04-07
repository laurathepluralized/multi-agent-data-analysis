/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { ExpManagerSqlTestModule } from '../../../test.module';
import { SimulationRunDialogComponent } from '../../../../../../main/webapp/app/entities/simulation-run/simulation-run-dialog.component';
import { SimulationRunService } from '../../../../../../main/webapp/app/entities/simulation-run/simulation-run.service';
import { SimulationRun } from '../../../../../../main/webapp/app/entities/simulation-run/simulation-run.model';

describe('Component Tests', () => {

    describe('SimulationRun Management Dialog Component', () => {
        let comp: SimulationRunDialogComponent;
        let fixture: ComponentFixture<SimulationRunDialogComponent>;
        let service: SimulationRunService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ExpManagerSqlTestModule],
                declarations: [SimulationRunDialogComponent],
                providers: [
                    SimulationRunService
                ]
            })
            .overrideTemplate(SimulationRunDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SimulationRunDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SimulationRunService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SimulationRun(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.simulationRun = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'simulationRunListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SimulationRun();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.simulationRun = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'simulationRunListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
