import { createRouter, createWebHistory } from 'vue-router'
import LayoutView from '../views/layout/index.vue'
import LoginView from '../views/login/index.vue'
import IndexView from '../views/index/index.vue'
import DepartmentsView from '../views/departments/index.vue'
import StaffView from '../views/staff/index.vue'
import MembersView from '../views/members/index.vue'
import TrainingCampsView from '../views/training-camps/index.vue'
import OperationLogsView from '../views/operation-logs/index.vue'
import AnalyticsStaffView from '../views/analytics/staff/index.vue'
import AnalyticsMembersView from '../views/analytics/members/index.vue'
import CoursesView from '../views/courses/index.vue'
import BookingsView from '../views/bookings/index.vue'
import MemberCardsView from '../views/member-cards/index.vue'
import EquipmentView from '../views/equipment/index.vue'
import FinancesView from '../views/finances/index.vue'

const routes = [
  {
    path: '/',
    component: LayoutView,
    redirect: '/index',
    children: [
      { path: '/index', name: 'index', component: IndexView },
      { path: '/departments', name: 'departments', component: DepartmentsView },
      { path: '/staff', name: 'staff', component: StaffView },
      { path: '/members', name: 'members', component: MembersView },
      { path: '/training-camps', name: 'trainingCamps', component: TrainingCampsView },
      { path: '/operation-logs', name: 'operationLogs', component: OperationLogsView },
      { path: '/analytics/staff', name: 'analyticsStaff', component: AnalyticsStaffView },
      { path: '/analytics/members', name: 'analyticsMembers', component: AnalyticsMembersView },
      { path: '/courses', name: 'courses', component: CoursesView },
      { path: '/bookings', name: 'bookings', component: BookingsView },
      { path: '/member-cards', name: 'memberCards', component: MemberCardsView },
      { path: '/equipment', name: 'equipment', component: EquipmentView },
      { path: '/finances', name: 'finances', component: FinancesView },
    ]
  },
  { path: '/login', name: 'login', component: LoginView }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('pulsefit_token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
