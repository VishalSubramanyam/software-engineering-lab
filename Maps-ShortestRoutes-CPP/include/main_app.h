#ifndef MAIN_APP_H
#define MAIN_APP_H
#include <wx/wx.h>
#include "application_frame.h"

class main_app : public wxApp
{
public:
  main_app();
  ~main_app();
  bool OnInit() override;
private:
  application_frame *app_frame = nullptr;
};

#endif //MAIN_APP_H
