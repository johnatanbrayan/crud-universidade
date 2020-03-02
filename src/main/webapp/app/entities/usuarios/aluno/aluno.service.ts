import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAluno } from '../../../shared/model/usuarios/aluno.model';

type EntityResponseType = HttpResponse<IAluno>;
type EntityArrayResponseType = HttpResponse<IAluno[]>;

@Injectable({ providedIn: 'root' })
export class AlunoService {
    public resourceUrl = SERVER_API_URL + 'api/alunos';

    constructor(protected http: HttpClient) {}

    create(aluno: IAluno): Observable<EntityResponseType> {
        return this.http.post<IAluno>(this.resourceUrl, aluno, { observe: 'response' });
    }

    update(aluno: IAluno): Observable<EntityResponseType> {
        return this.http.put<IAluno>(this.resourceUrl, aluno, { observe: 'response' });
    }

    find(login: string): Observable<HttpResponse<IAluno>> {
        return this.http.get<IAluno>(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAluno[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(login: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    authorities(): Observable<string[]> {
        return this.http.get<string[]>(SERVER_API_URL + 'api/alunos/authorities');
    }
}
