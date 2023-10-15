#include "main_app.h"
/**
 * This is a macro that sets up GUI related stuff by generating an entry point
 * for the program.
 */
wxIMPLEMENT_APP(main_app);
main_app::main_app() = default; ///< Default constructor
main_app::~main_app() = default; ///< Default destructor

/**
 * Initializes the application by setting up the main GUI
 * @return
 */
bool main_app::OnInit()
{
  app_frame = new application_frame();
  app_frame->Show();
  return true;
}
