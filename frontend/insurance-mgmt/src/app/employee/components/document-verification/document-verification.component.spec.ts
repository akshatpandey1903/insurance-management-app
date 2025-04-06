import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentVerificationComponent } from './document-verification.component';

describe('DocumentVerificationComponent', () => {
  let component: DocumentVerificationComponent;
  let fixture: ComponentFixture<DocumentVerificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DocumentVerificationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DocumentVerificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
