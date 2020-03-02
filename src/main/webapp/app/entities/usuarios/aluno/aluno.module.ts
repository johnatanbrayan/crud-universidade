import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CrudUniversidadeSharedModule } from '../../../shared/shared.module';

import { NgxViacepModule } from '@brunoc/ngx-viacep';
import { TextMaskModule } from 'angular2-text-mask';

import { AlunoComponent, AlunoUpdateComponent, AlunoDeleteDialogComponent, AlunoDetailComponent, alunoRoute } from './';

const ENTITY_STATES = [...alunoRoute];

@NgModule({
    imports: [CrudUniversidadeSharedModule, RouterModule.forChild(ENTITY_STATES), NgxViacepModule, TextMaskModule],
    declarations: [AlunoComponent, AlunoUpdateComponent, AlunoDeleteDialogComponent, AlunoDetailComponent],
    entryComponents: [AlunoComponent, AlunoUpdateComponent, AlunoDeleteDialogComponent, AlunoDetailComponent],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CrudUniversidadeAlunoModule {}
