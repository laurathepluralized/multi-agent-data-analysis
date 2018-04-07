import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { SimulationRun } from './simulation-run.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<SimulationRun>;

@Injectable()
export class SimulationRunService {

    private resourceUrl =  SERVER_API_URL + 'api/simulation-runs';

    constructor(private http: HttpClient) { }

    create(simulationRun: SimulationRun): Observable<EntityResponseType> {
        const copy = this.convert(simulationRun);
        return this.http.post<SimulationRun>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(simulationRun: SimulationRun): Observable<EntityResponseType> {
        const copy = this.convert(simulationRun);
        return this.http.put<SimulationRun>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<SimulationRun>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<SimulationRun[]>> {
        const options = createRequestOption(req);
        return this.http.get<SimulationRun[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<SimulationRun[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: SimulationRun = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<SimulationRun[]>): HttpResponse<SimulationRun[]> {
        const jsonResponse: SimulationRun[] = res.body;
        const body: SimulationRun[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to SimulationRun.
     */
    private convertItemFromServer(simulationRun: SimulationRun): SimulationRun {
        const copy: SimulationRun = Object.assign({}, simulationRun);
        return copy;
    }

    /**
     * Convert a SimulationRun to a JSON which can be sent to the server.
     */
    private convert(simulationRun: SimulationRun): SimulationRun {
        const copy: SimulationRun = Object.assign({}, simulationRun);
        return copy;
    }
}
