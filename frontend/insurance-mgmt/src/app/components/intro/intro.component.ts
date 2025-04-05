import { AfterViewInit, Component , Inject, PLATFORM_ID , Renderer2} from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-intro',
  standalone: false,
  templateUrl: './intro.component.html',
  styleUrl: './intro.component.css'
})
export class IntroComponent implements AfterViewInit {
  private isBrowser: boolean = false;
  private intervalId: any;
  private currentIndex = 0;
  private slides: HTMLElement[] = [];
  private dots: HTMLElement[] = [];

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private renderer: Renderer2
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  ngAfterViewInit(): void {
    if (this.isBrowser) {
      this.slides = Array.from(document.querySelectorAll('.slide-img'));
      this.dots = Array.from(document.querySelectorAll('.dot'));

      this.showSlide(this.currentIndex);
      this.setupDotClicks();
      this.startAutoSlide();
    }
  }

  showSlide(index: number): void {
    this.slides.forEach((slide, i) => {
      slide.classList.remove('active');
    });
    this.dots.forEach((dot, i) => {
      dot.classList.remove('active-dot');
    });

    this.slides[index].classList.add('active');
    this.dots[index].classList.add('active-dot');
    this.currentIndex = index;
  }

  setupDotClicks(): void {
    this.dots.forEach((dot, i) => {
      dot.addEventListener('click', () => {
        this.showSlide(i);
        this.restartAutoSlide();
      });
    });
  }

  startAutoSlide(): void {
    this.intervalId = setInterval(() => {
      const nextIndex = (this.currentIndex + 1) % this.slides.length;
      this.showSlide(nextIndex);
    }, 5000);
  }

  restartAutoSlide(): void {
    clearInterval(this.intervalId);
    this.startAutoSlide();
  }
}
