import { NgModule } from '@angular/core';

import { CrudUniversidadeSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [CrudUniversidadeSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [CrudUniversidadeSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class CrudUniversidadeSharedCommonModule {}
