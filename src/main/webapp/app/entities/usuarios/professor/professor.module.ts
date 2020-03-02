import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CrudUniversidadeSharedModule } from '../../../shared/shared.module';

import { NgxViacepModule } from '@brunoc/ngx-viacep';
import { TextMaskModule } from 'angular2-text-mask';

import { ProfessorComponent, ProfessorUpdateComponent, ProfessorDeleteDialogComponent, ProfessorDetailComponent, professorRoute } from './';

const ENTITY_STATES = [...professorRoute];

@NgModule({
    imports: [CrudUniversidadeSharedModule, RouterModule.forChild(ENTITY_STATES), NgxViacepModule, TextMaskModule],
    declarations: [ProfessorComponent, ProfessorUpdateComponent, ProfessorDeleteDialogComponent, ProfessorDetailComponent],
    entryComponents: [ProfessorComponent, ProfessorUpdateComponent, ProfessorDeleteDialogComponent, ProfessorDetailComponent],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CrudUniversidadeProfessorModule {}
