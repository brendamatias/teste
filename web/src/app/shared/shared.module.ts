import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MaterialModule } from './material.module';
import { SafeHtmlPipe } from './pipes/safeHtml/safeHtml.pipe';
import { LoadingDialog } from '../core/components/dialogs/loading/loading.dialog';

@NgModule({
    imports: [
        CommonModule,
        MaterialModule,
        RouterModule,
        ReactiveFormsModule,       
        FormsModule,
    ],
    schemas: [
        CUSTOM_ELEMENTS_SCHEMA
    ],
    declarations: [
        SafeHtmlPipe,
        LoadingDialog,
    ],
    exports: [
        CommonModule,
        MaterialModule,
        RouterModule,       
        SafeHtmlPipe 
    ],
})
export class SharedModule { }
