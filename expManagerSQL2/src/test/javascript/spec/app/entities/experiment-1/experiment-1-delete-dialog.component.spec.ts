/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { ExpManagerSql2TestModule } from '../../../test.module';
import { Experiment1DeleteDialogComponent } from '../../../../../../main/webapp/app/entities/experiment-1/experiment-1-delete-dialog.component';
import { Experiment1Service } from '../../../../../../main/webapp/app/entities/experiment-1/experiment-1.service';

describe('Component Tests', () => {

    describe('Experiment1 Management Delete Component', () => {
        let comp: Experiment1DeleteDialogComponent;
        let fixture: ComponentFixture<Experiment1DeleteDialogComponent>;
        let service: Experiment1Service;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ExpManagerSql2TestModule],
                declarations: [Experiment1DeleteDialogComponent],
                providers: [
                    Experiment1Service
                ]
            })
            .overrideTemplate(Experiment1DeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(Experiment1DeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(Experiment1Service);
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
