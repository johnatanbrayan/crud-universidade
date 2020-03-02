import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProfessor } from 'app/shared/model/usuarios/professor.model';

type EntityResponseType = HttpResponse<IProfessor>;
type EntityArrayResponseType = HttpResponse<IProfessor[]>;

@Injectable({ providedIn: 'root' })
export class ProfessorService {
    public resourceUrl = SERVER_API_URL + 'api/professores';

    constructor(protected http: HttpClient) {}

    create(professor: IProfessor): Observable<EntityResponseType> {
        return this.http.post<IProfessor>(this.resourceUrl, professor, { observe: 'response' });
    }

    update(professor: IProfessor): Observable<EntityResponseType> {
        return this.http.put<IProfessor>(this.resourceUrl, professor, { observe: 'response' });
    }

    find(login: string): Observable<HttpResponse<IProfessor>> {
        return this.http.get<IProfessor>(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProfessor[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    queryPorIdPaiResponsavelPage(req?: any): Observable<EntityArrayResponseType> {
        const resourceUrl = SERVER_API_URL + '/api/professores/porPaiPage';
        const options = createRequestOption(req);
        return this.http.get<IProfessor[]>(resourceUrl, { params: options, observe: 'response' });
    }

    queryPorIdPaiResponsavelList(req?: any): Observable<EntityArrayResponseType> {
        const resourceUrl = SERVER_API_URL + '/api/professores/porPaiList';
        const options = createRequestOption(req);
        return this.http.get<IProfessor[]>(resourceUrl, { params: options, observe: 'response' });
    }

    delete(login: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    authorities(): Observable<string[]> {
        return this.http.get<string[]>(SERVER_API_URL + 'api/professores/authorities');
    }
}
