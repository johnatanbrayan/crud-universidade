import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgxViacepModule } from '@brunoc/ngx-viacep';
import { TextMaskModule } from 'angular2-text-mask';
import { TelaInicialComponent } from 'app/entities/tela-inicial/tela-inicial.component';
import { telaInicialRoute } from 'app/entities/tela-inicial/tela-inicial.route';
import { CrudUniversidadeSharedModule } from 'app/shared';

const ENTITY_STATES = [...telaInicialRoute];

@NgModule({
    imports: [CrudUniversidadeSharedModule, RouterModule.forChild(ENTITY_STATES), NgxViacepModule, TextMaskModule],
    declarations: [TelaInicialComponent],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CrudUniversidadeTelaInicialModule {}
