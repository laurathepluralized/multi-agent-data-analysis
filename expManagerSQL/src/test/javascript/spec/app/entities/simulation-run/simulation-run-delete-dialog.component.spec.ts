/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { ExpManagerSqlTestModule } from '../../../test.module';
import { SimulationRunDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/simulation-run/simulation-run-delete-dialog.component';
import { SimulationRunService } from '../../../../../../main/webapp/app/entities/simulation-run/simulation-run.service';

describe('Component Tests', () => {

    describe('SimulationRun Management Delete Component', () => {
        let comp: SimulationRunDeleteDialogComponent;
        let fixture: ComponentFixture<SimulationRunDeleteDialogComponent>;
        let service: SimulationRunService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ExpManagerSqlTestModule],
                declarations: [SimulationRunDeleteDialogComponent],
                providers: [
                    SimulationRunService
                ]
            })
            .overrideTemplate(SimulationRunDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SimulationRunDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SimulationRunService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
