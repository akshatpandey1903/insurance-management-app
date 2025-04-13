import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResolveQueriesComponent } from './resolve-queries.component';

describe('ResolveQueriesComponent', () => {
  let component: ResolveQueriesComponent;
  let fixture: ComponentFixture<ResolveQueriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ResolveQueriesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResolveQueriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
