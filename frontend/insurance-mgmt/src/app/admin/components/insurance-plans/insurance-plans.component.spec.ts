import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InsurancePlansComponent } from './insurance-plans.component';

describe('InsurancePlansComponent', () => {
  let component: InsurancePlansComponent;
  let fixture: ComponentFixture<InsurancePlansComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InsurancePlansComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InsurancePlansComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
