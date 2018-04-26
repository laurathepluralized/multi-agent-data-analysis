import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Experiment1 } from './experiment-1.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Experiment1>;

@Injectable()
export class Experiment1Service {

    private resourceUrl =  SERVER_API_URL + 'api/experiment-1-s';

    constructor(private http: HttpClient) { }

    create(experiment1: Experiment1): Observable<EntityResponseType> {
        const copy = this.convert(experiment1);
        return this.http.post<Experiment1>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(experiment1: Experiment1): Observable<EntityResponseType> {
        const copy = this.convert(experiment1);
        return this.http.put<Experiment1>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Experiment1>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Experiment1[]>> {
        const options = createRequestOption(req);
        return this.http.get<Experiment1[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Experiment1[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Experiment1 = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Experiment1[]>): HttpResponse<Experiment1[]> {
        const jsonResponse: Experiment1[] = res.body;
        const body: Experiment1[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Experiment1.
     */
    private convertItemFromServer(experiment1: Experiment1): Experiment1 {
        const copy: Experiment1 = Object.assign({}, experiment1);
        return copy;
    }

    /**
     * Convert a Experiment1 to a JSON which can be sent to the server.
     */
    private convert(experiment1: Experiment1): Experiment1 {
        const copy: Experiment1 = Object.assign({}, experiment1);
        return copy;
    }
}
