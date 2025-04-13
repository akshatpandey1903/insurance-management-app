import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerQueriesComponent } from './customer-queries.component';

describe('CustomerQueriesComponent', () => {
  let component: CustomerQueriesComponent;
  let fixture: ComponentFixture<CustomerQueriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomerQueriesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerQueriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
