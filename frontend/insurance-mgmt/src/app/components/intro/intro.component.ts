import { Component } from '@angular/core';

@Component({
  selector: 'app-intro',
  standalone: false,
  templateUrl: './intro.component.html',
  styleUrl: './intro.component.css'
})
export class IntroComponent {
  slides = [
    { image: 'https://images.unsplash.com/photo-1506748686214-e9df14d4d9d0?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80' },
    { image: 'https://images.unsplash.com/photo-1521747116042-5a810fda9664?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80' },
    { image: 'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80' }
  ];

  features = [
    { title: 'Fast Claims', description: 'Process claims quickly and efficiently.', icon: 'fas fa-clock' },
    { title: 'Wide Coverage', description: 'Comprehensive plans for all your needs.', icon: 'fas fa-shield-alt' },
    { title: '24/7 Support', description: 'Get help anytime, anywhere.', icon: 'fas fa-headset' }
  ];

  currentSlide = 0;

  ngOnInit() {
    this.startSlideshow();
  }

  startSlideshow() {
    setInterval(() => {
      this.currentSlide = (this.currentSlide + 1) % this.slides.length;
    }, 5000); // Change slide every 5 seconds
  }

  setSlide(index: number) {
    this.currentSlide = index;
  }
}
